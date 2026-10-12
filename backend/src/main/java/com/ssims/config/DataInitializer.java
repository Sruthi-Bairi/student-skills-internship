package com.ssims.config;

import com.ssims.domain.Internship;
import com.ssims.domain.Role;
import com.ssims.domain.User;
import com.ssims.repo.InternshipRepository;
import com.ssims.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements ApplicationRunner {

	private final UserRepository userRepository;
	private final InternshipRepository internshipRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${ssims.admin.email}")
	private String adminEmail;

	@Value("${ssims.admin.password}")
	private String adminPassword;

	public DataInitializer(UserRepository userRepository, InternshipRepository internshipRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.internshipRepository = internshipRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (!userRepository.existsByEmailIgnoreCase(adminEmail)) {
			User admin = new User();
			admin.setEmail(adminEmail.toLowerCase());
			admin.setPassword(passwordEncoder.encode(adminPassword));
			admin.setRole(Role.ADMIN);
			userRepository.save(admin);
		}
		if (internshipRepository.count() == 0) {
			internshipRepository.save(internship(
					"Frontend Developer Intern",
					"Nova Labs",
					"Bengaluru",
					"Build responsive React interfaces for campus products.",
					"React, JavaScript, CSS",
					"3 months",
					"INR 15,000 / month",
					LocalDate.now().plusMonths(1)));
			internshipRepository.save(internship(
					"Java Backend Intern",
					"CampusWorks",
					"Hyderabad",
					"Work on Spring Boot REST APIs and MySQL schemas.",
					"Java, Spring Boot, MySQL",
					"6 months",
					"INR 20,000 / month",
					LocalDate.now().plusMonths(2)));
			internshipRepository.save(internship(
					"Full Stack Intern",
					"SkillBridge",
					"Remote",
					"Ship features across React and Spring Boot with code reviews.",
					"React, Java, REST",
					"4 months",
					"INR 18,000 / month",
					LocalDate.now().plusWeeks(6)));
		}
	}

	private Internship internship(String title, String company, String location, String description,
			String skills, String duration, String stipend, LocalDate deadline) {
		Internship item = new Internship();
		item.setTitle(title);
		item.setCompany(company);
		item.setLocation(location);
		item.setDescription(description);
		item.setRequiredSkills(skills);
		item.setDuration(duration);
		item.setStipend(stipend);
		item.setDeadline(deadline);
		item.setActive(true);
		return item;
	}
}
