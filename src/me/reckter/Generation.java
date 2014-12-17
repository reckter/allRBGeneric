package me.reckter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hannes on 16/12/14.
 */
public class Generation {

    public static final int STARTING_GENERATION = 40;

    public static final int SURVIVORS = 2;
    public static final int MUTATIONS = 4;
    public static final int MUTATIONS_PER_INDIVIDUUM = Picture.SIZE;

    public static int generation = 0;

    public Individuum best;

    List<Individuum> individuen;


    public Generation() {
        individuen = new ArrayList<Individuum>();
    }

    public void init() {
        Picture.setFile("pictures/original.jpg");
        for(int i = 0; i < STARTING_GENERATION; i++) {
             individuen.add(Individuum.createRandomPicture());
        }
    }


    public void nextGeneration() {
        generation++;
        Collections.sort(individuen);
        best = individuen.get(0);

        List<Individuum> surviors = individuen.subList(0, SURVIVORS);
        List<Individuum> mutations = new ArrayList<>(individuen.size());
        for(int i = 0; i < MUTATIONS; i++) {
            surviors.parallelStream().map(Individuum::new).forEach(mutations::add);
        }

        mutations.parallelStream().forEach(individuum -> {
            int rand = (int) (MUTATIONS_PER_INDIVIDUUM * Math.abs(Individuum.random.nextGaussian() + 10 / generation));
            if(rand > MUTATIONS_PER_INDIVIDUUM * 2) {
                rand = MUTATIONS_PER_INDIVIDUUM * 2;
            }
            individuum.mutate(rand);
        });

        individuen = mutations;
        individuen.addAll(surviors);
    }

    public void output() {
        best.output();
    }
}

