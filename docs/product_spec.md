# Clan Territory Capture -- Product Specification (MVP)

Version: 1.2\
Platform: Android (Kotlin)\
Primary Interface: Zoomable Stylized Map

------------------------------------------------------------------------

# 1. Overview

Clan Territory Capture is a location‑based campus game designed to drive
foot traffic to underutilized spaces.\
Players belong to one of four clans and earn XP by visiting physical
locations and completing small minigames.

Each day one **Key Space** is contested.\
At **18:00 IST** the clan with the highest XP captures that space.

Captured spaces persist until manually reset by the admin.

------------------------------------------------------------------------

# 2. Core Gameplay Loop

1.  Player opens the app → map screen loads.
2.  Player sees daily pins on the campus map.
3.  Player walks to a location.
4.  When within geofence radius → pin activates.
5.  Player completes a minigame.
6.  Win grants XP.
7.  Clan score updates in real time.
8.  At 18:00 IST the winning clan captures the key space.

------------------------------------------------------------------------

# 3. Time Rules

Gameplay Window\
08:00 -- 18:00 IST

Daily Reset\
18:00 IST

Event Windows (engagement boost)

10:40 -- 11:00\
12:40 -- 13:40\
17:00 -- 18:00

Notifications trigger before these windows.

------------------------------------------------------------------------

# 4. XP System

Win = 25 XP

Daily Player Cap = 100 XP

Clan Score = Sum of Player XP

XP resets daily.

------------------------------------------------------------------------

# 5. Pin Rules

Loss • Location locks for that player until reset.

Win • Location cooldown = 5 minutes.

Pin Pool • Admin creates a daily pool. • Each player receives a subset.
• Overlaps between players allowed.

Typical allocation • \~6 pins per player.

------------------------------------------------------------------------

# 6. Location Verification

Start condition requires:

Distance ≤ 25 meters from pin center

GPS accuracy ≤ 15 meters

Two consecutive stable fixes within 10 seconds.

If accuracy degrades, start button disables.

------------------------------------------------------------------------

# 7. Map System

The map is the **primary screen**.

Implementation

Stylized campus image\
Overlay grid system\
Sprite overlays

The map is **zoomable and pannable**.

Zoom levels must preserve pixel clarity.

Rendering must use **nearest‑neighbor filtering**.

------------------------------------------------------------------------

# 8. Grid System

Grid Size\
32 × 32 px

Assets snap to grid.

Rotation Supported

0°\
90°\
180°\
270°

Assets may occupy multiple tiles.

Examples

1×1 tile\
1×2 tile\
2×2 tile

------------------------------------------------------------------------

# 9. Placement Zones

Only **Key Spaces** allow placement.

Admin defines for each space:

grid_origin_x\
grid_origin_y\
grid_width\
grid_height

Example

Courtyard = 10×10 tiles

Different spaces may have different grid sizes.

------------------------------------------------------------------------

# 10. Cosmetic Asset System

Assets are **player specific**.

Players place objects in captured spaces.

Other players do **not see** these placements.

This keeps the system simple and avoids multiplayer state conflicts.

Assets come from:

Rare chest drops\
Minigame rewards

Assets expire if unplaced at the next daily reset.

Placed assets persist until:

space ownership resets\
or season resets

------------------------------------------------------------------------

# 11. Animation System

Lightweight animation only.

Supported types

Player Marker • Idle bob animation

Ambient Props • tree sway • chest sparkle • banner flutter • rune glow

UI Effects • pin highlight bounce • placement preview pulse

Animation Format

Sprite frames\
8 FPS ambient loops\
12 FPS highlight loops

------------------------------------------------------------------------

# 12. Minigames

Minigames are Kotlin native.

Examples

Word puzzle\
Pattern matching\
Logic puzzle\
Mini Sudoku\
Tile matching

Characteristics

Timed\
Fixed difficulty\
Offline execution allowed

Results must sync online.

Pool size

\~10 games

3--5 randomly shown per location.

------------------------------------------------------------------------

# 13. Notification System

Types

Daily Start\
08:15

Event Window Alerts\
10:35\
12:35\
16:55

Final Hour Alert\
17:00

Results Notification\
18:00

Next Key Space Announcement\
20:00

Location proximity notifications are **not used**.

------------------------------------------------------------------------

# 14. Anti‑Cheat

Basic protections

Speed check between sessions\
Location accuracy validation\
Cooldown enforcement server‑side\
Daily XP cap server‑side

------------------------------------------------------------------------

# 15. Admin Dashboard

Admin capabilities

Create / edit pins\
Select daily pin pool\
Define key space\
Configure event windows\
Reset space ownership

Reports

Live clan score\
Daily winner report\
Player participation stats

Asset Export

Per clan

player_id\
asset_name\
count\
timestamp

Used for physical campus leaderboard.

------------------------------------------------------------------------

# 16. Technical Architecture

Client

Android Kotlin App

UI Jetpack Compose

Rendering Compose Canvas

Location Android Fused Location Provider

Backend

API Server

Stores

players\
pins\
sessions\
xp\
assets\
clans

Real‑time updates

WebSocket or Firebase.

------------------------------------------------------------------------

# 17. Data Model (Core)

Players

player_id\
email\
clan\
display_name

Pins

pin_id\
latitude\
longitude\
active_day

Sessions

session_id\
player_id\
pin_id\
result\
timestamp

Assets

asset_id\
player_id\
asset_type\
placed_location

Clans

clan_id\
name\
score

------------------------------------------------------------------------

# 18. MVP Acceptance Criteria

User can login using college email.

Map loads with zoom and pan.

Pins appear and activate when in range.

Minigame win grants XP.

Daily XP cap enforced.

Clan score updates live.

18:00 winner capture works.

Admin can configure pins without app update.

Asset placements work on grid.

Animations render smoothly.

------------------------------------------------------------------------

# 19. Recommended Implementation Order

Phase 1\
Auth + clan assignment

Phase 2\
Map rendering + zoom

Phase 3\
Pin system + geofence

Phase 4\
Minigame launcher

Phase 5\
XP scoring

Phase 6\
Daily capture logic

Phase 7\
Asset placement

Phase 8\
Admin dashboard

Phase 9\
Notifications

Phase 10\
Animation polish
