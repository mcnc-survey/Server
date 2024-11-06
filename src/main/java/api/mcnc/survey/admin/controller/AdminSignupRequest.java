package api.mcnc.survey.admin.controller;

import api.mcnc.survey.admin.repository.entity.Role;

public record AdminSignupRequest(
    String email,
    String password,
    Role role
) { }
