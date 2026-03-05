# Dev Tasks – Clan Territory Capture (MVP)
Source of truth: `docs/product_spec.md`

Principle: build step-by-step. Each step must compile and run before moving on.

---

## Step 0 — Repo Setup
Acceptance:
- Repo has `docs/` with `product_spec.md` and this file.
- Android module builds in Android Studio.

Tasks:
- [ ] Create Android project (Kotlin, Gradle Kotlin DSL).
- [ ] Enable Jetpack Compose.
- [ ] Basic app icon + app name.

---

## Step 1 — Map Engine v1 (Zoom/Pan + Overlays)
Goal: map-first screen with transform gestures and placeholder overlays.

Tasks:
- [ ] `MapScreen` as start destination.
- [ ] Render stylized map image (local asset).
- [ ] Implement pinch-zoom + pan using Compose transform gestures.
- [ ] Nearest-neighbor rendering (pixel crispness).
- [ ] Coordinate system:
  - world coordinates in map pixels (top-left origin)
  - screen mapping depends on pan/zoom
- [ ] Overlay:
  - Player marker sprite at fixed world position (placeholder)
  - Idle bob animation (sinusoidal y offset)
- [ ] Pins:
  - Hardcode 6 pins with world pixel coordinates
  - Render pin icons
  - Tap detection → show bottom sheet with pin info/state
- [ ] Grid overlay toggle:
  - 32x32px grid drawn over map
  - Toggle in UI

Acceptance:
- App launches into MapScreen.
- Zoom/pan smooth; pins stay anchored correctly.
- Pin tapping works.
- Grid toggle works.
- Player marker animates.

Deliverable:
- PR/commit “Map Engine v1”.

---

## Step 2 — Location Layer v1 (Foreground GPS + Player Position)
Goal: show real GPS position mapped into world coordinates (for now, via calibration).

Key decision:
- Real GPS (lat/lng) must map to stylized map pixels via calibration points.
- Use 2-point affine mapping MVP (or 3+ points if you implement a better transform).

Tasks:
- [ ] Foreground location permission flow.
- [ ] Fused Location Provider (high accuracy).
- [ ] Location updates on MapScreen (foreground only).
- [ ] Add MapCalibration:
  - At minimum: 2 known anchor points (lat/lng ↔ world pixel)
  - Convert user lat/lng to world pixel coordinate
- [ ] Render player marker at computed world position.
- [ ] Debug panel:
  - accuracy meters, lat/lng, mapped world coord
  - current zoom, pan

Acceptance:
- On campus, player marker moves as user moves.
- No background location required.

Deliverable:
- PR/commit “GPS Foreground + Calibration”.

---

## Step 3 — Pins v2 (Server-like Config + Pin States)
Goal: represent pins as data, not hardcoded; implement state machine (available/in-range/cooldown/locked).

Tasks:
- [ ] Local JSON config loader (pretend server):
  - pins list
  - daily key space (placeholder)
  - event windows times (placeholder)
- [ ] Pin state model:
  - Available / InRange / Cooldown / Locked
- [ ] Geofence logic (MVP rules):
  - distance <= 25m AND accuracy <= 15m
  - require 2 stable fixes within 10s
- [ ] Pin highlight + Start enabled when InRange.
- [ ] Pin details sheet shows state, cooldown timer, lock info.

Acceptance:
- Walking near a pin triggers InRange highlight.
- Start button only enabled under rules.

Deliverable:
- PR/commit “Pins + Geofence Rules”.

---

## Step 4 — Minigame Shell (Kotlin Native)
Goal: framework to plug in NYT-style minigames; implement 1 sample minigame stub.

Tasks:
- [ ] Define minigame interface:
  - id, title, time_limit
  - start(), submit(), result
- [ ] Build “MinigameHostScreen” that launches from a pin.
- [ ] Implement 1 trivial minigame (e.g., tap sequence within time).
- [ ] Result posting is local only (no backend yet).
- [ ] Enforce:
  - Win -> +25 XP
  - Loss -> lock pin until reset
  - Win -> cooldown 5 min

Acceptance:
- From an in-range pin, a minigame launches.
- Results update local state correctly.

Deliverable:
- PR/commit “Minigame Host + 1 Game”.

---

## Step 5 — XP + Clan Score (Local Simulation)
Goal: implement XP cap, clan scoring, and 18:00 reset logic locally.

Tasks:
- [ ] Player daily XP cap = 100 enforced.
- [ ] Clan score computed and shown (local mock for now).
- [ ] Daily reset scheduler:
  - at 18:00 IST, reset locks/cooldowns/xp
  - show “Results” modal
- [ ] Key space capture local simulation.

Acceptance:
- XP stops at 100.
- 18:00 reset triggers and clears daily state.

Deliverable:
- PR/commit “Daily Scoring + Reset”.

---

## Step 6 — Assets + Placement (Local)
Goal: placeable cosmetics in key spaces only, with 32x32 grid snapping and 4-way rotation.

Tasks:
- [ ] Define key space placeable zone:
  - grid origin (x,y), width, height in tiles
- [ ] Placement UI:
  - select asset from inventory
  - preview ghost sprite
  - snap to grid
  - rotate 0/90/180/270
  - validate bounds and collisions
- [ ] Inventory:
  - earn “chest drops” as random rewards on wins (local)
  - unplaced assets expire at daily reset
- [ ] Persist placed assets locally per user per captured space.

Acceptance:
- Player can place multiple assets into tiles.
- Rotation works.
- Unplaced assets reset at daily reset.

Deliverable:
- PR/commit “Placement v1”.

---

## Step 7 — Backend + Admin Dashboard (Separate modules)
Goal: replace local config/state with real backend and admin tools.

Tasks:
- [ ] Backend services:
  - config (pins, key space, event windows)
  - sessions (minigame results)
  - scoring (clan totals)
  - assets (earned list)
- [ ] Admin dashboard:
  - manage pins
  - set daily pool + key space
  - reset ownership
  - export assets per player per clan

Acceptance:
- App consumes config from backend.
- Admin can change pins without app update.

Deliverable:
- PR/commit “Backend integration + Admin”.

---

## Definition of Done for each step
- Builds successfully
- Runs on device
- No “TODO crashes”
- Minimal logging + a debug panel where needed
- One commit/PR per step