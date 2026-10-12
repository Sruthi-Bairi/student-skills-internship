package com.ssims.service;

import com.ssims.domain.Internship;
import com.ssims.dto.InternshipRequest;
import com.ssims.dto.InternshipResponse;
import com.ssims.repo.InternshipRepository;
import com.ssims.web.ApiException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

@Service
public class InternshipService {

	private final InternshipRepository internshipRepository;

	public InternshipService(InternshipRepository internshipRepository) {
		this.internshipRepository = internshipRepository;
	}

	@Transactional(readOnly = true)
	public List<InternshipResponse> search(String query, String location, Boolean activeOnly) {
		Specification<Internship> spec = (root, cq, cb) -> cb.conjunction();
		if (Boolean.TRUE.equals(activeOnly)) {
			spec = spec.and((root, cq, cb) -> cb.isTrue(root.get("active")));
		}
		if (StringUtils.hasText(query)) {
			String like = "%" + query.trim().toLowerCase(Locale.ROOT) + "%";
			spec = spec.and((root, cq, cb) -> cb.or(
					cb.like(cb.lower(root.get("title")), like),
					cb.like(cb.lower(root.get("company")), like),
					cb.like(cb.lower(root.get("requiredSkills")), like),
					cb.like(cb.lower(root.get("description")), like)
			));
		}
		if (StringUtils.hasText(location)) {
			String like = "%" + location.trim().toLowerCase(Locale.ROOT) + "%";
			spec = spec.and((root, cq, cb) -> cb.like(cb.lower(root.get("location")), like));
		}
		return internshipRepository.findAll(spec).stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public InternshipResponse get(Long id) {
		return toResponse(require(id));
	}

	@Transactional
	public InternshipResponse create(InternshipRequest request) {
		return toResponse(internshipRepository.save(fromRequest(new Internship(), request)));
	}

	@Transactional
	public InternshipResponse update(Long id, InternshipRequest request) {
		Internship internship = require(id);
		return toResponse(internshipRepository.save(fromRequest(internship, request)));
	}

	@Transactional
	public void delete(Long id) {
		try {
			internshipRepository.delete(require(id));
			internshipRepository.flush();
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {
			throw new ApiException(HttpStatus.CONFLICT, "Cannot delete an internship that already has applications");
		}
	}

	public Internship require(Long id) {
		return internshipRepository.findById(id)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Internship not found"));
	}

	private Internship fromRequest(Internship internship, InternshipRequest request) {
		internship.setTitle(request.title().trim());
		internship.setCompany(request.company().trim());
		internship.setLocation(blankToNull(request.location()));
		internship.setDescription(request.description().trim());
		internship.setRequiredSkills(blankToNull(request.requiredSkills()));
		internship.setDuration(blankToNull(request.duration()));
		internship.setStipend(blankToNull(request.stipend()));
		internship.setDeadline(request.deadline());
		internship.setActive(request.active() == null || request.active());
		return internship;
	}

	public InternshipResponse toResponse(Internship internship) {
		return new InternshipResponse(
				internship.getId(),
				internship.getTitle(),
				internship.getCompany(),
				internship.getLocation(),
				internship.getDescription(),
				internship.getRequiredSkills(),
				internship.getDuration(),
				internship.getStipend(),
				internship.getDeadline(),
				internship.isActive()
		);
	}

	private String blankToNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim();
	}
}
