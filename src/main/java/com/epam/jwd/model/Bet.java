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
    private final BetType betType;
    private final Person person;

    public Bet(Long id, Betslip betslip, Integer betTotal, BetType betType, Person person) {
        super(id);
        this.betslip = betslip;
        this.betTotal = betTotal;
        this.betType = betType;
        this.person = person;
    }

    public Bet(Betslip betslip, Integer betTotal, BetType betType, Person person) {
        this(null, betslip, betTotal, betType, person);
    }

    public Bet(Betslip betslip, Person person) {
        this(null, betslip, null, null, person);
    }

    public Betslip getBetslip() {
        return betslip;
    }

    public Integer getBetTotal() {
        return betTotal;
    }

    public BetType getBetType() {
        return betType;
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
        return Objects.equals(betslip, bet.betslip) && Objects.equals(betTotal, bet.betTotal) && betType == bet.betType && Objects.equals(person, bet.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), betslip, betTotal, betType, person);
    }

    @Override
    public String toString() {
        return "Betslip: " + betslip +
                ", Total: " + betTotal +
                ", Type: " + betType +
                ", Person: " + person;
    }

}