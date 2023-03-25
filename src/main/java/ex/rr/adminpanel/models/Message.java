package ex.rr.adminpanel.models;

import java.time.Instant;

public record Message(String username, String text, Instant time) {
}

