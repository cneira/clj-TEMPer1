# clj-TEMPer1

A Clojure library designed to discover and read temperature from TEMPer1 USB devives

## Usage

`lein run` will call both the `list-devices` and `read-temperature` functions and log the results.
`lein test` will make calls agains the same functions, hence they'll only work with the TEMPer1 device connected. They also tests the internal methods for converting the read bytes, raw temperature result etc.

## License

[WTFPL – Do What the Fuck You Want to Public License](http://www.wtfpl.net/)
