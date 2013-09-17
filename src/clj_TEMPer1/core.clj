(ns clj-TEMPer1.core
  (:import [com.codeminders.hidapi HIDDeviceInfo HIDManager]
           [java.io IOException])
  (:use [clojure.pprint :as pprint]
        [clojure.string :only (join)]
        [clojure.tools.logging :only (info error)]))


(defn list-devices []
  (try
    (let [manager (HIDManager/getInstance)
          devs (.listDevices manager)]
      (info "TEMPer1 devices:\n")
      (info (join "\n\n" (filter #(re-matches #".*TEMPer1.*" (str %)) devs))))
    (catch IOException e (str "caught exception: " (.getMessage e)))))

(def VENDOR_ID 3141)
(def PRODUCT_ID 29697)
(def USAGE_PAGE 65280)
(def BUFSIZE 2048)
(def READ_UPDATE_DELAY_MS 50)

(def temper1-pattern (re-pattern (str ".*TEMPer.*usage_page=" USAGE_PAGE ".*")))

(defn open-temper1 []
  (let [manager (HIDManager/getInstance)
        devs (.listDevices manager)
        temper1 (first (filter #(re-matches temper1-pattern (str %)) devs))]
    (info "\nFound TEMPer1:")
    (pprint temper1)
    (.open temper1)))

(defn raw-temp [buf]
  (let [b1 (bit-and (aget buf 3)(byte -1))
        b2 (bit-shift-left (aget buf 2) 8)]
    (+ b1 b2)))

(defn neg-magnitude-temp [buf]
  (let [magnitude-temp (bit-xor (raw-temp buf) 65535)]
    (- (+ magnitude-temp 1))))

(defn negate-temp [buf]
  (not= (bit-and (aget buf 2) -128) 0))

(defn raw-to-c [raw-temp]
  (let [celcius (float (* raw-temp (/ 125 32000)))]
  (info (str "Temp: " celcius "C"))))

(defn print-celcius [temp]
  (raw-to-c temp))

(def temp (byte-array (map byte [1, -128, 51, 1, 0, 0, 0, 0])))

(defn read-device []
  (try
     (if-let [dev (open-temper1)]
       (do
         (.write dev temp)

         (try
           (info (str "\nReading temperature from " (.getProductString dev)))
           (.disableBlocking dev)
           (let [buf (byte-array BUFSIZE)]

             (loop [res (.readTimeout dev buf 5000)]
               (if (= res 0)
                 (recur (.readTimeout dev buf 5000))))

             (if (negate-temp buf)
               (print-celcius (neg-magnitude-temp buf))
               (print-celcius (raw-temp buf))))
         (finally (do
                    (.close dev)
                    (.release (HIDManager/getInstance))))))
       (error "Found no TEMPer1 device!"))
   (catch IOException e (str "caught IOException: " (.getMessage e)))))


(defn -main [& args]
;;   (System/setProperty "java.library.path" "native/macosx/x86_64/")
  (info "Load hidapi-jni library")
  (info (System/getProperty "java.library.path"))
  (info "---------------")
  (clojure.lang.RT/loadLibrary "hidapi-jni-64")
;;  (list-devices)
;;   (read-device)
  (System/exit 0))
