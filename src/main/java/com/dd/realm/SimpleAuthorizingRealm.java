package com.dd.realm;

import com.dd.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.integration.shiro.SimpleShiroToken;
import org.nutz.mvc.Mvcs;

public class SimpleAuthorizingRealm extends AuthorizingRealm {
    protected Dao dao;


    /**
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store.  When returning
     * an instance from this method, you might want to consider using an instance of
     * {@link SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see SimpleAuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        return null;
    }

    /**
     * Retrieves authentication data from an implementation-specific datasource (RDBMS, LDAP, etc) for the given
     * authentication token.
     * <p/>
     * For most datasources, this means just 'pulling' authentication data for an associated subject/user and nothing
     * more and letting Shiro do the rest.  But in some systems, this method could actually perform EIS specific
     * log-in logic in addition to just retrieving data - it is up to the Realm implementation.
     * <p/>
     * A {@code null} return value means that no account could be associated with the specified token.
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return an {@link AuthenticationInfo} object containing account data resulting from the
     * authentication ONLY if the lookup is successful (i.e. account exists and is valid, etc.)
     * @throws AuthenticationException if there is an error acquiring data or performing
     *                                 realm-specific authentication logic for the specified <tt>token</tt>
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String)token.getPrincipal();  //得到用户名
        String password = new String((char[])token.getCredentials()); //得到密码
        User u2 = dao().fetch(User.class, Cnd.where(User.USERNAME,"=",username).and(User.PWD,"=",password));
        int count = dao.count(User.class, Cnd.where(User.USERNAME, "=", username));
        if(count == 0 ){//用户名不存在
            throw new UnknownAccountException(); //如果用户名错误
        }
        if(u2 == null){//密码错误
            throw new IncorrectCredentialsException(); //如果密码错误
        }

        //如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(username, password, getName());
    }

    /**
     * 覆盖父类的验证,直接pass. 在shiro内做验证的话, 出错了都不知道哪里错
     */
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
    }

    public SimpleAuthorizingRealm() {
        this(null, null);
    }

    public SimpleAuthorizingRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super(cacheManager, matcher);
        setAuthenticationTokenClass(SimpleShiroToken.class); // 非常非常重要,与SecurityUtils.getSubject().login是对应关系!!!
    }

    public SimpleAuthorizingRealm(CacheManager cacheManager) {
        this(cacheManager, null);
    }

    public SimpleAuthorizingRealm(CredentialsMatcher matcher) {
        this(null, matcher);
    }

    public Dao dao() {
        if (dao == null) {
            dao = Mvcs.ctx().getDefaultIoc().get(Dao.class, "dao");
            return dao;
        }
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if(token instanceof UsernamePasswordToken){
            return true;
        }
        return false;
    }
}
