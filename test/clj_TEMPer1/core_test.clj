(ns clj-TEMPer1.core-test
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


(def celcius (read-temperature))
(expect Float celcius)
