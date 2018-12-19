package com.teasoft.rydeon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.teasoft.auth.model.Role;
import com.teasoft.auth.model.UserRole;
import com.teasoft.auth.model.Users;
import com.teasoft.auth.sec.PasswordHash;
import com.teasoft.auth.service.RoleService;
import com.teasoft.auth.service.UserRoleService;
import com.teasoft.auth.service.UsersService;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.service.PersonService;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@ComponentScan({"com.teasoft.auth", "com.teasoft.rydeon"})
@EntityScan({"com.teasoft.auth", "com.teasoft.rydeon"})
@EnableJpaRepositories({"com.teasoft.auth", "com.teasoft.rydeon"})
public class RydeonApplication {

    public static void main(String[] args) {
        SpringApplication.run(RydeonApplication.class, args);
    }

    @Bean
    @Primary
    public DriverManagerDataSource getMySQLDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:mysql://localhost/rydeon");
        dataSource.setUsername("root");
        return dataSource;
    }

    @RequestMapping("/health")
    @ResponseBody
    public String health(HttpServletRequest req, HttpServletResponse res) {
        return "health ok";
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(createHttpConnector());
        return tomcat;
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setSecure(true);
        connector.setPort(5000);
        connector.setRedirectPort(443);
        return connector;
    }

    @Bean
    public InitializingBean insertDefaultUsers() {
        return new InitializingBean() {
            @Autowired
            RoleService roleService;
            @Autowired
            PersonService personService;
            @Autowired
            UsersService userService;
            @Autowired
            UserRoleService userRoleService;

            @Override
            public void afterPropertiesSet() throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
                Role role, role2, roleUser;
                if ((role = roleService.findByRoleName("ADMIN")) == null) {
                    role = new Role();
                    role.setRoleName("ADMIN");
                    role = roleService.save(role);
                }

                if ((role2 = roleService.findByRoleName("USERADMIN")) == null) {
                    role2 = new Role();
                    role2.setRoleName("USERADMIN");
                    role2 = roleService.save(role2);
                }
                
                if((roleUser = roleService.findByRoleName("USER")) == null) {
                    roleUser = new Role();
                    roleUser.setRoleName("USER");
                    roleUser = roleService.save(roleUser);
                }

                Users user;
                if ((user = userService.findByPhone("233242724203")) == null) {
                    user = new Users();
                    user.setEnabled(Boolean.TRUE);
                    user.setAccountNonExpired(Boolean.TRUE);
                    user.setAccountNonLocked(Boolean.TRUE);
                    user.setCredentialNonExpired(Boolean.TRUE);
                    user.setPassword(PasswordHash.createHash("unityn"));
                    user.setPhone("233242724203");
                    user.setEmail("elikemteddy@gmail.com");
                    user.setUsername("elikemt");
                    user = userService.save(user);
                }
                
                Person person;
                if ((person = personService.findByUser(user)) == null) {
                    person = new Person();
                    person.setFirstname("Theodore");
                    person.setLastname("Attigah");
                    person.setOthernames("Elikem");
                    person.setPhone("233242724203");
                    person.setEmail("elikemteddy@gmail.com");
                    person.setUser(userService.save(user));
                    person.setVerified(Boolean.TRUE);
                }
                
                personService.savePerson(person, user);

                if (userRoleService.findByUserAndRole(user, role) == null) {
                    UserRole userRole = new UserRole();
                    userRole.setRole(role);
                    userRole.setUser(user);
                    userRoleService.save(userRole);
                }

                if (userRoleService.findByUserAndRole(user, role2) == null) {
                    UserRole userRole = new UserRole();
                    userRole.setRole(role2);
                    userRole.setUser(user);
                    userRoleService.save(userRole);
                }
                
                if (userRoleService.findByUserAndRole(user, roleUser) == null) {
                    UserRole userRole = new UserRole();
                    userRole.setRole(roleUser);
                    userRole.setUser(user);
                    userRoleService.save(userRole);
                }
            }
        };
    }
}
