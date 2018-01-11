package com.yudi;

import com.yudi.backend.persistence.domain.backend.Role;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.persistence.domain.backend.UserRole;
import com.yudi.backend.service.PlanService;
import com.yudi.backend.service.UserService;
import com.yudi.enums.PlansEnum;
import com.yudi.enums.RolesEnum;
import com.yudi.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SpringAwsStripeApplication implements CommandLineRunner{

	/*the application logger*/
	private static final Logger LOG = LoggerFactory.getLogger(SpringAwsStripeApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringAwsStripeApplication.class, args);
	}

	@Autowired
	private UserService userService;

    @Autowired
    private PlanService planService;

    @Value("${webmaster.username}")
    private String webmasterUsername;

    @Value("${webmaster.password}")
    private String webmasterPassword;

    @Value("${webmaster.email}")
    private String webmasterEmail;


	@Override
	public void run(String... strings) throws Exception {
        LOG.info("Create basic and pro plans in the database ");
        planService.createPlan(PlansEnum.BASIC.getId());
        planService.createPlan(PlansEnum.PRO.getId());

		User basicUser = UserUtils.createBasicUser(webmasterUsername,webmasterEmail);
        basicUser.setPassword(webmasterPassword);
        Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(basicUser, new Role(RolesEnum.ADMIN)));
		LOG.debug("Create user with username {}", basicUser.getUsername());
		userService.createUser(basicUser, PlansEnum.PRO, userRoles);
		LOG.debug("User created {}", basicUser.getUsername());

	}
}
