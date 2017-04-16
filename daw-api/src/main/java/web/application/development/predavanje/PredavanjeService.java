package web.application.development.predavanje;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredavanjeService {
	
	@Autowired //injects instance of predmetRepository
	private PredavanjeRepository predmetRepository;

	public List<Predavanje> getAllPredavanje() {
		List<Predavanje> predavanje = new ArrayList<>();
		predmetRepository.findAll().forEach(predavanje::add);
		return predavanje;
	}
	
	public Predavanje getPredavanje(String predmetId) {
		//return topics.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		return predmetRepository.findOne(predmetId);
	}

	public void addPredavanje(Predavanje predmet) {
		predmetRepository.save(predmet);
	}
	
	public void updatePredavanje(String id, Predavanje predmet) {
		predmetRepository.save(predmet);
	}

	public void deletePredavanje(String id) {
		predmetRepository.delete(id);
	}
	
	public void addTeamToPredavanje(String id, Predavanje predmet) {
		predmetRepository.save(predmet);
	}
	
	public void removeTeamfromPredavanje(String id, Predavanje predmet) {
		predmetRepository.save(predmet);
	}
	
	public void enrollStudentIntoClass(String id, Predavanje predmet) {
		predmetRepository.save(predmet);
	}
	
	public void assignTeacherToClass(String id, Predavanje predmet) {
		predmetRepository.save(predmet);
	}
}
