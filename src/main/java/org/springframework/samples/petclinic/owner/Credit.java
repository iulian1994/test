/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.NamedEntity;

@Entity
@Table(name = "credits")
public class Credit extends NamedEntity {

    @Column(name = "credit_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creditDate;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private String type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Client owner;

    public LocalDate getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(LocalDate creditDate) {
        this.creditDate = creditDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    
}
