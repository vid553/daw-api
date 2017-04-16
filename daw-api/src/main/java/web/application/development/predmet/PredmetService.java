package web.application.development.predmet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.application.development.student.Student;

@Service
public class PredmetService {
	
	@Autowired //injects instance of predmetRepository
	private PredmetRepository predmetRepository;

	public List<Predmet> getAllPredmeti() {
		List<Predmet> predmet = new ArrayList<>();
		predmetRepository.findAll().forEach(predmet::add);
		return predmet;
	}
	
	public Predmet getPredmet(String predmetId) {
		//return topics.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		return predmetRepository.findOne(predmetId);
	}

	public void addPredmet(Predmet predmet) {
		predmetRepository.save(predmet);
	}
	
	public void updatePredmet(String id, Predmet predmet) {
		predmetRepository.save(predmet);
	}

	public void deletePredmet(String id) {
		predmetRepository.delete(id);
	}
	
	public void addTeamToPredmet(String id, Predmet predmet) {
		predmetRepository.save(predmet);
	}
	
	public void removeTeamfromPredmet(String id, Predmet predmet) {
		predmetRepository.save(predmet);
	}
	
	public void enrollStudentIntoClass(String id, Predmet predmet) {
		predmetRepository.save(predmet);
	}
	
	public void assignTeacherToClass(String id, Predmet predmet) {
		predmetRepository.save(predmet);
	}
}
