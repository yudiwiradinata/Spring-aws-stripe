package com.yudi.web.controllers;

import com.yudi.backend.persistence.domain.backend.Plan;
import com.yudi.backend.persistence.domain.backend.Role;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.persistence.domain.backend.UserRole;
import com.yudi.backend.service.PlanService;
import com.yudi.backend.service.S3Service;
import com.yudi.backend.service.UserService;
import com.yudi.enums.PlansEnum;
import com.yudi.enums.RolesEnum;
import com.yudi.utils.Constans;
import com.yudi.utils.UserUtils;
import com.yudi.web.domain.frontend.BasicAccountPayload;
import com.yudi.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Yudi on 04/01/2018.
 */
@Controller
public class SignupController {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private PlanService planService;

    @Autowired
    private UserService userService;

    @Autowired
    private S3Service s3Service;

    @RequestMapping(value = Constans.SignUp.SIGNUP_URL_MAPPING, method = RequestMethod.POST)
    public String signupPost(@RequestParam(name = "planId", required = true) int planId,
                             @RequestParam(name = "file", required = false) MultipartFile file,
                             @ModelAttribute(Constans.SignUp.PAYLOAD_MODEL_KEY_NAME) @Valid ProAccountPayload payload
                             ,ModelMap model) throws IOException{
        if(planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()){
            model.addAttribute(Constans.SignUp.SIGNED_UP_MESSAGE_KEY,"false");
            model.addAttribute(Constans.SignUp.ERROR_MESSAGE_KEY,"Plan id does not exist");
            return Constans.SignUp.SUBSCRIPTION_VIEW_NAME;
        }

        this.checkForDuplicates(payload, model);

        boolean duplicates = false;

        List<String> errorMessages = new ArrayList<>();

        if(model.containsKey(Constans.SignUp.DUPLICATED_USERNAME_KEY)){
            LOG.warn("The username already exist");
            model.addAttribute(Constans.SignUp.SIGNED_UP_MESSAGE_KEY,"false");
            errorMessages.add("Username already exist");
            duplicates = true;
        }

        if(model.containsKey(Constans.SignUp.DUPLICATED_EMAIL_KEY)){
            LOG.warn("The email already exist");
            model.addAttribute(Constans.SignUp.SIGNED_UP_MESSAGE_KEY,"false");
            errorMessages.add("Email already exist");
            duplicates = true;
        }

        if(duplicates){
            model.addAttribute(Constans.SignUp.ERROR_MESSAGE_KEY, errorMessages);
            return Constans.SignUp.SUBSCRIPTION_VIEW_NAME;
        }

        LOG.debug("Transforming user payload to domain object");
        User user = UserUtils.fromWebUserToDomainUser(payload);

        // stores the profile images on amazon s3
        if(file != null && !file.isEmpty()){
            String profileImageUrl = s3Service.storeProfileName(file, payload.getUsername());
            if(profileImageUrl != null){
                user.setProfileImageUrl(profileImageUrl);
            }else{
                LOG.warn("problem on uploading to amazon s3");
            }

        }

        // sets plan and the roles
        LOG.debug("Retrieving plan from database");
        Plan selectedPlan = planService.findByPlanId(planId);
        if(null == selectedPlan){
            LOG.error("The plan id {} could not be found", planId);
            model.addAttribute(Constans.SignUp.SIGNED_UP_MESSAGE_KEY,"false");
            model.addAttribute(Constans.SignUp.ERROR_MESSAGE_KEY, "Plan id not found");
            return Constans.SignUp.SUBSCRIPTION_VIEW_NAME;
        }
        user.setPlan(selectedPlan);
        User registeredUser = null;

        //by default users get the basic role
        Set<UserRole> roles = new HashSet<>();
        if(planId == PlansEnum.BASIC.getId()){
            roles.add(new UserRole(user, new Role(RolesEnum.BASIC)));
            registeredUser = userService.createUser(user, PlansEnum.BASIC, roles);
        }else if(planId == PlansEnum.PRO.getId()){
            roles.add(new UserRole(user, new Role(RolesEnum.PRO)));
            registeredUser = userService.createUser(user, PlansEnum.PRO, roles);
            LOG.debug(payload.toString());
        }

        //Auto login registered user
        Authentication auth = new UsernamePasswordAuthenticationToken(
          registeredUser,null,registeredUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        LOG.info("User created successfully");
        model.addAttribute(Constans.SignUp.SIGNED_UP_MESSAGE_KEY,"true");

        return  Constans.SignUp.SUBSCRIPTION_VIEW_NAME;
    }

    @RequestMapping(value = Constans.SignUp.SIGNUP_URL_MAPPING, method = RequestMethod.GET)
    public String signupGet(@RequestParam("planId") int planId, ModelMap model){

        if(planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()){
            throw new IllegalArgumentException("Plan is not valid");
        }

        model.addAttribute(Constans.SignUp.PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());

        return Constans.SignUp.SUBSCRIPTION_VIEW_NAME;
    }

    private void checkForDuplicates(BasicAccountPayload payload, ModelMap model){
        //username
        if(userService.findByUsername(payload.getUsername()) != null){
            model.addAttribute(Constans.SignUp.DUPLICATED_USERNAME_KEY,true);
        }
        if(userService.findByEmail(payload.getEmail()) != null){
            model.addAttribute(Constans.SignUp.DUPLICATED_EMAIL_KEY,true);
        }
    }
}
