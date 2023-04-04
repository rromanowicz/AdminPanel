package ex.rr.adminpanel.datasource.models;

import java.time.Instant;

public record Message(String username, String text, Instant time) {
}

