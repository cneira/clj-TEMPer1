(ns clj-TEMPer1.core
  (:import [com.codeminders.hidapi HIDDeviceInfo HIDManager]
           [java.io IOException])
  (:use [clojure.pprint :as pprint]
        [clojure.string :only (join)]
        [clojure.tools.logging :only (debug info error)]))

(def VENDOR_ID 3141)
(def PRODUCT_ID 29697)
(def USAGE_PAGE 65280)
(def BUFSIZE 2048)
(def READ_UPDATE_DELAY_MS 50)

(def temper1-pattern (re-pattern (str ".*TEMPer.*usage_page=" USAGE_PAGE ".*")))

(defn- open-temper1 []
  (let [manager (HIDManager/getInstance)
        devs (.listDevices manager)]
    (if-let [temper1 (first (filter #(re-matches temper1-pattern (str %)) devs))]
      (.open temper1)
      (throw (RuntimeException. "No TEMPer1 device found!!")))))

(defn- raw-temp [buf]
  (let [b1 (bit-and (aget buf 3)(byte -1))
        b2 (bit-shift-left (aget buf 2) 8)]
    (+ b1 b2)))

(defn- neg-magnitude-temp [buf]
  (let [magnitude-temp (bit-xor (raw-temp buf) 65535)]
    (- (+ magnitude-temp 1))))

(defn- negate-temp [buf]
  (if (not= (bit-and (aget buf 2) -128) 0)
    (neg-magnitude-temp buf)))

(defn- raw-to-c [raw-temp]
  (let [celcius (float (* raw-temp (/ 125 32000)))]
    celcius))

(defn- calc-celcius [buf]
  (raw-to-c (if-let [neg-celcius (negate-temp buf)]
    neg-celcius
    (raw-temp buf))))

(def temp (byte-array (map byte [1, -128, 51, 1, 0, 0, 0, 0])))

(defn read-temperature []
  (try
     (if-let [dev (open-temper1)]
       (do
         (.write dev temp)

         (try
           (debug (str "Reading temperature from " (.getProductString dev)))
           (.disableBlocking dev)
           (let [buf (byte-array BUFSIZE)]

             (loop [res (.readTimeout dev buf 5000)]
               (if (= res 0)
                 (recur (.readTimeout dev buf 5000))))
             (pprint buf)
             (let [celcius (calc-celcius buf)]
               celcius))
         (finally (do
                    (.close dev)
                    (.release (HIDManager/getInstance))))))
       (error "Found no TEMPer1 device!"))
   (catch IOException e (str "caught IOException: " (.getMessage e)))))


(defn list-devices []
  (try
    (let [manager (HIDManager/getInstance)
          devs (.listDevices manager)]
      (filter #(re-matches #".*TEMPer1.*" (str %)) devs))
    (catch IOException e (str "caught exception: " (.getMessage e)))))

(defn load-hid-natives []
   (clojure.lang.RT/loadLibrary "hidapi-jni-64"))


(defn -main [& args]
  (info (str "Loading hidapi-jni library from: " (System/getProperty "java.library.path")))
  (load-hid-natives)
  (info (str "Devices found: \n" (join "\n\n" (list-devices))))
  (info (str "Temp: " (read-temperature) "°C"))
  (System/exit 0))
