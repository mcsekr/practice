package in.co.practice.test.encryption.rest.controller;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import in.co.practice.test.encryption.rest.model.Payload;

@RestController
public class EncryptionRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionRestController.class);
	private final static String REGEX_PATTERN_ALPHABETS = "^[a-zA-Z]*$";
	private final static String EMPTY_INPUT_ERROR_MESSAGE = "Input string is empty or null";
	private final static String NONALPHABET_INPUT_ERROR_MESSAGE = "Only aplhabets are allowed)";

	@PostMapping(value = "encryption-count")
	public String encryptedWordCount(@RequestBody Payload payload) {
		LOGGER.info(" encryptedWordCount start() =>>");
		StringBuffer encryptedLettersWithCount = new StringBuffer();
		if (payload == null || payload.getPayloadBody() == null) {
			LOGGER.error("Invalid body empty or null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMPTY_INPUT_ERROR_MESSAGE);
		}
		final String inputBody = payload.getPayloadBody();
		if (inputBody.matches(REGEX_PATTERN_ALPHABETS)) {
			final String ecnryptedString = Base64.getEncoder().encodeToString(inputBody.getBytes());
			final String letterCountString = fetchLetterCountFromString(inputBody);
			encryptedLettersWithCount.append(letterCountString);
		} else {
			LOGGER.error("Invalid body includes non alphabets");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NONALPHABET_INPUT_ERROR_MESSAGE);
		}
		LOGGER.info(" encryptedWordCount end() =>>");
		return encryptedLettersWithCount.toString();
	}

	private String fetchLetterCountFromString(String input) {
		LOGGER.info(" fetchLetterCountFromString Srart() =>>");
		Map<Character, Integer> output = new LinkedHashMap<Character, Integer>();
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			output.compute(ch, (key, value) -> (value == null) ? 1 : value + 1);
		}
		StringBuffer outputString = new StringBuffer();
		for (Character character : output.keySet()) {
			Integer count = output.get(character);
			outputString.append(character).append(count);
		}
		LOGGER.info(" fetchLetterCountFromString End() =>>");
		return outputString.toString();
	}

}
