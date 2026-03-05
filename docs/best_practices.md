# Best Practices – Campus Territory Game (Android/Kotlin)

This repo is built step-by-step. Each step must compile and run on a device/emulator before moving forward.

## Repo workflow
- One feature step per branch + PR (or per commit if you’re solo).
- Keep commits small and reversible.
- Require “app launches” as a gate for every merge.

Branch naming
- feat/step-1-map-engine
- feat/step-2-gps-calibration
- feat/step-3-geofence-states
- fix/...
- chore/...

Commit messages
- Step-based, descriptive:
  - "Step 1: map engine v1 (zoom/pan + pins)"
  - "Step 2: fused location + calibration"

## Code structure (Android)
Use a stable package layout early:
- ui/
  - map/
  - components/
  - theme/
- domain/
  - model/
  - logic/
- data/
  - local/
  - remote/ (later)
- core/
  - util/
  - time/
  - location/

Guidelines
- Keep UI state in ViewModels.
- Prefer immutable UI state data classes.
- Avoid passing Android Context deep into domain layers.

## Compose + rendering
- Prefer a single “MapRenderer” composable that draws:
  1) background image
  2) grid overlay (optional)
  3) pins
  4) player marker
  5) placement previews (later)
- Keep coordinate transform logic isolated:
  - world(map px) -> screen(px)
  - screen(px) -> world(map px)

Pixel art rule
- Ensure bitmap scaling uses nearest-neighbor (no smoothing).
- Avoid blurry zoom. Use integer-ish zoom steps if needed (e.g., clamp to 0.5x increments).

## Performance
- Cache decoded bitmaps. Do not decode every recomposition.
- Avoid allocating objects in hot draw loops.
- Use stable collections (`ImmutableList` pattern) for pins if you can.
- Keep FPS stable during pinch-zoom: prefer coarse grid drawing when zoomed out.

## Location + permissions
- Foreground location only for MVP.
- Always show accuracy meters + last update time in a debug panel.
- Apply your start rules exactly:
  - distance <= 25m
  - accuracy <= 15m
  - 2 stable fixes within 10s
- Debounce updates to reduce UI churn (e.g., 250–500ms).

## Time handling
- Treat IST as authoritative for game logic.
- Always compute server time later (backend step). For now:
  - Use device time but isolate time calls behind `TimeProvider`.

## Anti-cheat (MVP)
- Enforce all caps server-side later, but also enforce locally:
  - daily XP cap
  - cooldown
  - lock
- Log anomaly signals, but don’t block aggressively in MVP.

## Testing approach
Minimum per-step tests:
- Unit test coordinate transforms.
- Unit test geofence eligibility given distance/accuracy/fixes.
- Manual smoke test checklist:
  - app launches
  - zoom/pan smooth
  - pins anchored
  - tap works
  - permissions flow works (step 2+)

## “Definition of Done” per step
- Builds (Debug)
- Runs on emulator AND at least one physical Android device
- No crashes on rotate/background/foreground
- Feature flag or debug toggles for dev-only UI
- Short README update per step