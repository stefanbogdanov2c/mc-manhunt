# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.3.5] - 2025-06-14

### Added

- Added the accuser role - the selected player can check if a player is a hunter, at the price of his life.
- Added a lightning sound when a hunted player dies.

## [1.3.4] - 2025-06-14

### Added

- Added a winning mechanism for the hunter players.

### Changed

- Changed the way respawning the players upon game completion is handled.

## [1.3.3] - 2025-06-14

### Added

- Added a winning mechanism for hunters.

## [1.3.2] - 2025-05-25

### Changed

- Changed the tracknearest timeout to 30sec instead of 2min.

## [1.3.1] - 2025-05-17

### Fixed

- Fix tracknearest command not working.

## [1.3.0] - 2025-05-17

### Changed

- Changed the mod name locally.
- Changed the workflow and build.gradle file so now the tag is added dynamically.

## [1.2.2] - 2025-05-17

### Changed

- Changed the gitignore file to allow gradle wrapper.

## [1.2.1] - 2025-05-17

### Changed

-Fixed build failing.

## [1.2.0] - 2025-05-17

### Added

- Added a workflow which triggers the building of the mod once a tag has been pushed on GitHub.

## [1.1.0] - 2025-05-17

### Added

- Added a 'stophunt' command, which stops the hunt.
- Added a 'tracknearest' command, a command only the hunters can use with a cooldown to find other players.

### Changed

- Hide the player names when clicking tab once the game has started.
- 'starthunt' command now supports multiple hunters.

## [1.0.0] - 2025-05-14

### Added

- The initial MC Reverse Manhunt release.

[unreleased]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.3.5...HEAD
[1.3.5]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.3.44...v1.3.5
[1.3.4]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.3.3...v1.3.4
[1.3.3]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.3.2...v1.3.3
[1.3.2]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.3.1...v1.3.2
[1.3.1]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.3.0...v1.3.1
[1.3.0]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.2.1...v1.3.0
[1.2.2]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.2.1...v1.2.2
[1.2.1]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/stefanbogdanov2c/mc-manhunt/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/stefanbogdanov2c/mc-manhunt/tree/v1.0.0