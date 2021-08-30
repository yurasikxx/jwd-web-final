package com.epam.jwd.model;

import java.util.Objects;

/**
 * Class {@code Betslip} is totalizator bet.
 * Extends {@code AbstractBaseEntity} class.
 *
 * @see AbstractBaseEntity
 * @see Betslip
 * @see Person
 */
public class Bet extends AbstractBaseEntity {

    private final Betslip betslip;
    private final Integer betTotal;
    private final Person person;

    public Bet(Long id, Betslip betslip, Integer betTotal, Person person) {
        super(id);
        this.betslip = betslip;
        this.betTotal = betTotal;
        this.person = person;
    }

    public Bet(Betslip betslip, Integer betTotal, Person person) {
        this(null, betslip, betTotal, person);
    }

    public Bet(Betslip betslip, Person person) {
        this(null, betslip, null, person);
    }

    public Betslip getBetslip() {
        return betslip;
    }

    public Integer getBetTotal() {
        return betTotal;
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
        return Objects.equals(betslip, bet.betslip) && Objects.equals(betTotal, bet.betTotal) && Objects.equals(person, bet.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), betslip, betTotal, person);
    }

    @Override
    public String toString() {
        return "Betslip: " + betslip +
                ", Total: " + betTotal +
                ", Person: " + person;
    }

}