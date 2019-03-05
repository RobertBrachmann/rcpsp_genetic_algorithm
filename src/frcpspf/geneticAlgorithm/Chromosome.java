package frcpspf.geneticAlgorithm;


public class Chromosome {

    // Chromosome id
    private Integer chromosomeId;

    public Chromosome(Integer chromosomeId){
        this.chromosomeId = chromosomeId;
    }

    @Override
    public String toString(){
        return "Chromosome id:      " + this.chromosomeId;
    }
}
