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

    /**
     * A constructor with ID used for pull up bet from database.
     *
     * @param id       a bet ID.
     * @param betslip  a bet betslip.
     * @param betTotal a bet total.
     * @param person   a bet person.
     */
    public Bet(Long id, Betslip betslip, Integer betTotal, Person person) {
        super(id);
        this.betslip = betslip;
        this.betTotal = betTotal;
        this.person = person;
    }

    /**
     * A constructor without ID used for save bet to database.
     *
     * @param betslip  a bet betslip.
     * @param betTotal a bet total.
     * @param person   a bet person.
     */
    public Bet(Betslip betslip, Integer betTotal, Person person) {
        this(null, betslip, betTotal, person);
    }

    /**
     * A constructor used for placing bet by user.
     *
     * @param betslip a bet betslip.
     * @param person  a bet person.
     */
    public Bet(Betslip betslip, Person person) {
        this(null, betslip, null, person);
    }

    /**
     * @return a bet betslip.
     */
    public Betslip getBetslip() {
        return betslip;
    }

    /**
     * @return a bet total.
     */
    public Integer getBetTotal() {
        return betTotal;
    }

    /**
     * @return a bet person.
     */
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