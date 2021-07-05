package com.epam.jwd.model;

import java.util.Objects;

public class Bet extends AbstractBaseEntity {

    private final Integer betTotal;
    private final Betslip betslip;
    private final Person person;

    public Bet(Long id, Integer betTotal, Betslip betslip, Person person) {
        super(id);
        this.betTotal = betTotal;
        this.betslip = betslip;
        this.person = person;
    }

    public Integer getBetTotal() {
        return betTotal;
    }

    public Betslip getBetslip() {
        return betslip;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bet bet = (Bet) o;
        return Objects.equals(betTotal, bet.betTotal) && Objects.equals(betslip, bet.betslip) && Objects.equals(person, bet.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), betTotal, betslip, person);
    }

    @Override
    public String toString() {
        return "Bet{" +
                "betTotal=" + betTotal +
                ", betslip=" + betslip +
                ", person=" + person +
                '}';
    }

}