(defproject clj-TEMPer1 "0.1.0-SNAPSHOT"
  :description "A small clojure program that interfaces with a TEMPer1 device"
  :license {:name "WTFPL – Do What the Fuck You Want to Public License"
            :url "http://www.wtfpl.net/"}
  :repositories [["ailis" "http://nexus.ailis.de/content/groups/public"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.codeminders/hidapi "1.1"]]
  :main clj-TEMPer1.core
  :jvm-opts ["-Djava.library.path=target/native/mac"])
