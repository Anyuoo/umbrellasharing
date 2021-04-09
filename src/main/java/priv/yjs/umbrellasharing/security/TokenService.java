package priv.yjs.umbrellasharing.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.model.entity.LoginUser;
import priv.yjs.umbrellasharing.security.jwt.JwtProperties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * token 服务层
 *
 * @author Anyu
 * @since 2021/3/24
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
@Log4j2
public class TokenService {
    private static final String USER_TOKEN_KEY = "token-key";
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private CacheService cacheService;


    /**
     * 获取登录用户
     */
    public Optional<LoginUser> getLoginUser(HttpServletRequest request) {
        return getTokenWith(request).map(token -> {
            var userToken = getUserToken(Objects.requireNonNull(parseJwt(token).orElse(null)));
            return (LoginUser) cacheService.strGet(userToken);
        });
    }

    /**
     * 创建jwt token
     */
    public Optional<String> createJwtToken(LoginUser loginUser) {
        final var userToken = UUID.randomUUID().toString();
        loginUser.setToken(userToken);
        final var jwt = createJwt(loginUser);
        cacheService.strPut(userToken, loginUser);
        return jwt;
    }


    /**
     * 创建jwt
     */
    private Optional<String> createJwt(LoginUser loginUser) {
        try {
            final var keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
            final var secretKey = Keys.hmacShaKeyFor(keyBytes);
            //添加构成JWT的参数
            final var builder = Jwts.builder()
                    // 可以将基本不重要的对象信息放到claims
                    .setHeaderParam("typ", "JWT")
                    .setHeaderParam("alg", "HS256")
                    .claim(USER_TOKEN_KEY, loginUser.getToken())
                    .setSubject(loginUser.getUsername())           // 代表这个JWT的主体，即它的所有人
                    .setIssuer(jwtProperties.getIss())              // 代表这个JWT的签发主体；
                    .setIssuedAt(new Date())        // 是一个时间戳，代表这个JWT的签发时间；
                    .setAudience(jwtProperties.getAud())          // 代表这个JWT的接收对象；
                    .signWith(secretKey);
            //添加Token过期时间
            final var nowMillis = System.currentTimeMillis();
            final var now = new Date(nowMillis);
            final var expiredHours = jwtProperties.getExpiredHours();
            if (expiredHours != null && expiredHours.toMillis() > 0) {
                final var expDuration = expiredHours.plusMillis(nowMillis);
                final var expMillis = expDuration.toMillis();
                final var exp = new Date(expMillis);
                builder.setExpiration(exp)  // 是一个时间戳，代表这个JWT的过期时间；
                        .setNotBefore(now); // 是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
            }
            //生成JWT
            return Optional.ofNullable(builder.compact());
        } catch (Exception e) {
            throw GlobalException.causeBy(ResultType.AUTH_ERROR);
        }
    }

    /**
     * 解析jwt
     */
    private Optional<Claims> parseJwt(String token) {
        try {
            final var claims = Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.ofNullable(claims);
        } catch (Exception e) {
            log.error("jwt 解析失败 ,message:{}", e.getMessage());
            throw GlobalException.causeBy(ResultType.AUTH_ERROR);
        }
    }

    /**
     * 通过httpServletRequest 获取token
     */
    private Optional<String> getTokenWith(HttpServletRequest httpServletRequest) {
        var header = httpServletRequest.getHeader(jwtProperties.getAuthHeaderKey());
        return Objects.isNull(header) ? Optional.empty() : getTokenWith(header);
    }

    /**
     * 通过header获取token
     */
    private Optional<String> getTokenWith(String authHeader) {
        var tokenPrefix = jwtProperties.getTokenPrefix();
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith(tokenPrefix)) {
            return Optional.of(authHeader.substring(tokenPrefix.length()));
        }
        return Optional.empty();
    }

    private String getUserToken(Claims claims) {
        return (String) claims.get(USER_TOKEN_KEY);
    }

}
