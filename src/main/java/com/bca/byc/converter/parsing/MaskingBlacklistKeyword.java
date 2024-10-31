package com.bca.byc.converter.parsing;

import com.bca.byc.entity.BlacklistKeyword;
import com.bca.byc.repository.BlacklistKeywordRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class MaskingBlacklistKeyword {

    public static String sanitizeWord(String words, BlacklistKeywordRepository blacklistRepository) {
        List<String> blacklistWords = blacklistWords(blacklistRepository);

        for (String word : blacklistWords) {
            String regex = "\\b" + word + "\\b"; // Regex similar to \bword\b
            String replacement = "*".repeat(word.length());
            words = words.replaceAll("(?i)" + regex, replacement);
        }
        return words;
    }

    public static List<String> blacklistWords(BlacklistKeywordRepository blacklistRepository) {
        return blacklistRepository.findAll()
                .stream().map(b-> StringUtils.lowerCase(b.getKeyword())).collect(Collectors.toList());
    }


}
