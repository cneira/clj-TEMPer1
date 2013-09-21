(ns clj-TEMPer1.core-test
  (:require [clj-TEMPer1.core :refer :all])
  (:use expectations))

(defn load-test-data
  "loads test data"
  {:expectations-options :before-run}
  []
  (clojure.lang.RT/loadLibrary "hidapi-jni-64"))


(def first-device (-> (first (list-devices))
                               (str)))
(expect #"TEMPer1" first-device)
(expect #"vendor_id=3141" first-device)
(expect #"product_id=29697" first-device)

(def celcius (read-device))
(expect Float celcius)
