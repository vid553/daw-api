package web.application.development.predmet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
