package com.evo.evoproject.service.user;

import java.util.Map;

public interface TermsService {
    Map<String, String> getLatestTerms(String termsType);
}
