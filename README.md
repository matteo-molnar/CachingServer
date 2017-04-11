# SENG401-G4_CachingServer

CacheController has is now fully integrated into the RequestHandler.

As of writing this, CacheController "should" be able to save responses into a file(somewhere) locally.

The file name consists of the query parameters mashed together.

PS: Sorry for spaghetti code

Update:
CacheController is fully integrated.
Caching period is by default 5 minutes (see TimerControl class)

Noted Defects:
Outdated caching entries are not deleted and remain in the system.
