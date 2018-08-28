
package com.test;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;



public class EventSummary {

    private String id;

    private Integer duration;

    private String host;

    private String type;

    private boolean alert;


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


    public String getHost() {
        return host;
    }


    public void setHost(String host) {
        this.host = host;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    @Override
    public String toString() {
        return "Event[ID:"+ id + ",duration=" + duration + ",host=" + host + ",type=" + type + ",alert=" + alert + "]";

    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(duration)
                .append(alert)
                .append(host)
                .append(type)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EventSummary) == false) {
            return false;
        }
        EventSummary rhs = ((EventSummary) other);
        return new EqualsBuilder().append(id, rhs.id)
                .append(duration, rhs.duration)
                .append(alert, rhs.alert)
                .append(host, rhs.host)
                .append(type, rhs.type)
                .isEquals();
    }
}
