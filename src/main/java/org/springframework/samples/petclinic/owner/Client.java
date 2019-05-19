package org.springframework.samples.petclinic.owner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.model.Company;


@Entity
@Table(name = "client")
public class Client extends Company {
    @Column(name = "address")
    @NotEmpty
    private String address;

    @Column(name = "city")
    @NotEmpty
    private String city;

    @Column(name = "telephone")
    @NotEmpty
    @Digits(fraction = 0, integer = 10)
    private String telephone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<Credit> credits;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    protected Set<Credit> getCreditsInternal() {
        if (this.credits == null) {
            this.credits = new HashSet<>();
        }
        return this.credits;
    }

    protected void setCreditsInternal(Set<Credit> credits) {
        this.credits = credits;
    }

    public List<Credit> getCredits() {
        List<Credit> sortedCredits = new ArrayList<>(getCreditsInternal());
        PropertyComparator.sort(sortedCredits,
                new MutableSortDefinition("name", true, true));
        return Collections.unmodifiableList(sortedCredits);
    }

    public void addCredit(Credit credit) {
        if (credit.isNew()) {
            getCreditsInternal().add(credit);
        }
        credit.setOwner(this);
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     *
     * @param name to test
     * @return true if pet name is already in use
     */
    public Credit getCredit(String name) {
        return getCredit(name, false);
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     *
     * @param name to test
     * @return true if pet name is already in use
     */
    public Credit getCredit(String name, boolean ignoreNew) {
        name = name.toLowerCase();
        for (Credit credit : getCreditsInternal()) {
            if (!ignoreNew || !credit.isNew()) {
                String compName = credit.getName();
                compName = compName.toLowerCase();
                if (compName.equals(name)) {
                    return credit;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)

                .append("id", this.getId()).append("new", this.isNew())
                .append("companyName", this.getCompanyName())
                .append("amount", this.getAmount()).append("address", this.address)
                .append("city", this.city).append("telephone", this.telephone).toString();
    }
}
