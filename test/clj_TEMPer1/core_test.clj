(ns clj-temper1.core-test
  (:require [clj-TEMPer1.core :refer :all])
  (:use expectations))

;; Have to load nativ hid libraries during compilation
(load-hid-natives)

;; Testing the list-devices function
(def first-device (-> (first (list-devices))
                               (str)))
(expect #"TEMPer1" first-device)
(expect #"vendor_id=3141" first-device)
(expect #"product_id=29697" first-device)

;; Testing reading a temperature
(def celcius (read-temperature))
(expect Float celcius)

;; Testing the conversion of the bytes
;; [-128, 2, 22, 80, -4, 82, -95, -40, 0, 0, 0, 0] into 22.3125°C
(def read-temp-buf (byte-array (map byte [-128, 2, 22, 80, -4, 82, -95, -40, 0, 0, 0, 0])))

(expect 22.3125 (#'clj-TEMPer1.core/calc-celcius read-temp-buf))
