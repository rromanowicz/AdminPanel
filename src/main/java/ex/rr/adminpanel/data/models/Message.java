package ex.rr.adminpanel.data.models;

import java.time.Instant;

public record Message(String username, String text, Instant time) {
}

