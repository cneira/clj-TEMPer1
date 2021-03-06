(defproject org.clojars.torbjornvatn/clj-temper1 "0.1.4-SNAPSHOT"
  :description "A small clojure library that interfaces with a TEMPer1 device"
  :license {:name "WTFPL – Do What the Fuck You Want to Public License"
            :url "http://www.wtfpl.net/"}
  :url "https://github.com/torbjornvatn/clj-TEMPer1"
  :repositories [["ailis" "http://nexus.ailis.de/content/groups/public"]
                 ["releases" {:url "https://clojars.org/repo"
                             :creds :gpg}]]
  :lein-release {:deploy-via :clojars}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojars.torbjornvatn/hidapi "1.1"]
                 [org.clojure/tools.logging "0.2.6"]
                 [org.slf4j/slf4j-log4j12 "1.7.5"]
                 [expectations "1.4.52"]]
  :aot [clj-temper1.core]
  :main clj-temper1.core)
