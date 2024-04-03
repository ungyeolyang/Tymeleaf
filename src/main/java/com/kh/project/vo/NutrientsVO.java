package com.kh.project.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutrientsVO implements Comparable<NutrientsVO>{
    private String nutrientsName;
    private String ingredientA;
    private String ingredientB;
    private String company;
    private String howToTake;

    @Override
    public int compareTo(NutrientsVO o) {
        if (this.nutrientsName.equals(o.nutrientsName)) return 0;
        else return 1;
    }
}
