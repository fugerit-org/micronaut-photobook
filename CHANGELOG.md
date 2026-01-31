# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.2] - 2026-01-31

### Changed

- JVM base image amazoncorretto:21.0.10-alpine3.23

### Fixed

- security issues from GitHub

## [1.1.1] - 2026-01-31

### Fixed

- jacoco dependency version set to 0.8.14

## [1.1.0] - 2026-01-29

### Changed

- micronaut version set to 4.10.7
- mongo db 8.2.3

## [1.0.1] - 2025-02-17

### Added

- docker compose native 

### Changed

- using actions/setup-java for native build
- fj-core version set to 8.6.6
- micronaut version set to 4.7.6
- switched to mongodb container tag 8.0

### Fixed

- MONGODB_URL environment in docker compose

## [Unreleased]

## [1.0.0] - 2024-06-30

### Added

- micronaut photobook project
- native image optimization instruction
- benchmark scripts
- docker build workflows
- Google App Engine (GAE) deployment note
- KNative deployment note