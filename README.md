# MACA Make Java Bean Observerable !!!

## TODO List
1. Drop `PropertyChangeListener` of `Java`
2. Redefine `DynamicList`: use proxy object to store elements, and store source list, generate change events by comparing source list and proxy object, the events should be:
  * `INSERT`: fire when some elements is inserted into List
  * `SET`: fire when some elements are replaced
  * `ADD`: fire when some elements are added in the end
  * `CHANGE`: other events caused list change
3. Redefine `DynamicMap`: use proxy object to store elements, and store source map, generate change events by comparing source map and proxy map
