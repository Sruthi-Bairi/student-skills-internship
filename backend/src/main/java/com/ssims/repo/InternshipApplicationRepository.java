package com.ssims.repo;

import com.ssims.domain.ApplicationStatus;
import com.ssims.domain.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {

	boolean existsByStudentIdAndInternshipId(Long studentId, Long internshipId);

	@Query("""
			select a from InternshipApplication a
			join fetch a.student
			join fetch a.internship
			where a.student.id = :studentId
			order by a.appliedAt desc
			""")
	List<InternshipApplication> findByStudentIdOrderByAppliedAtDesc(@Param("studentId") Long studentId);

	@Query("""
			select a from InternshipApplication a
			join fetch a.student
			join fetch a.internship
			order by a.appliedAt desc
			""")
	List<InternshipApplication> findAllByOrderByAppliedAtDesc();

	long countByStatus(ApplicationStatus status);
}
