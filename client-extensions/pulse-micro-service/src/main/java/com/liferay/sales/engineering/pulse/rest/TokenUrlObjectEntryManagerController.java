package com.liferay.sales.engineering.pulse.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/tokens/proxy")
@RestController
public class TokenUrlObjectEntryManagerController extends BaseController {
    private static final Log _log = LogFactory.getLog(
            TokenUrlObjectEntryManagerController.class);
    private static final Map<String, Map<String, JSONObject>>
            _objectEntryJSONObjectsMap = new HashMap<>();

    private JSONObject _getObjectEntryJSONObject(String json) {
        JSONObject jsonObject = new JSONObject(json);

        JSONObject objectEntryJSONObject = jsonObject.getJSONObject(
                "objectEntry");

        if (objectEntryJSONObject == null) {
            throw new IllegalArgumentException("Object entry is null");
        }

        return objectEntryJSONObject;
    }

    private Map<String, JSONObject> _getObjectEntryJSONObjects(
            String objectDefinitionExternalReferenceCode) {

        return _objectEntryJSONObjectsMap.computeIfAbsent(
                objectDefinitionExternalReferenceCode, key -> new HashMap<>());
    }

    @DeleteMapping(
            "/{objectDefinitionExternalReferenceCode}/{externalReferenceCode}"
    )
    public ResponseEntity<String> delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String objectDefinitionExternalReferenceCode,
            @PathVariable String externalReferenceCode,
            @RequestParam Map<String, String> parameters) {

        log(jwt, _log, parameters);

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{objectDefinitionExternalReferenceCode}")
    public ResponseEntity<String> get(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String objectDefinitionExternalReferenceCode,
            @RequestParam Map<String, String> parameters) {

        log(jwt, _log, parameters);

        Map<String, JSONObject> objectEntryJSONObjects =
                _getObjectEntryJSONObjects(objectDefinitionExternalReferenceCode);

        return new ResponseEntity<>(
                new JSONObject(
                ).put(
                        "items", objectEntryJSONObjects.values()
                ).put(
                        "totalCount", objectEntryJSONObjects.size()
                ).toString(),
                HttpStatus.OK);
    }

    @GetMapping(
            "/{objectDefinitionExternalReferenceCode}/{externalReferenceCode}"
    )
    public ResponseEntity<String> get(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String objectDefinitionExternalReferenceCode,
            @PathVariable String externalReferenceCode,
            @RequestParam Map<String, String> parameters) {

        log(jwt, _log, parameters);

        Map<String, JSONObject> objectEntryJSONObjects =
                _getObjectEntryJSONObjects(objectDefinitionExternalReferenceCode);

        JSONObject objectEntryJSONObject = objectEntryJSONObjects.get(
                externalReferenceCode);

        if (objectEntryJSONObject == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                objectEntryJSONObject.toString(), HttpStatus.OK);
    }

    @PostMapping("/{objectDefinitionExternalReferenceCode}")
    public ResponseEntity<String> post(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String objectDefinitionExternalReferenceCode,
            @RequestBody String json) {

        log(jwt, _log, json);

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping(
            "/{objectDefinitionExternalReferenceCode}/{externalReferenceCode}"
    )
    public ResponseEntity<String> put(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String objectDefinitionExternalReferenceCode,
            @PathVariable String externalReferenceCode, @RequestBody String json) {

        log(jwt, _log, json);

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
