(defproject usbtesting "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["ailis" "http://nexus.ailis.de/content/groups/public"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.codeminders/hidapi "1.1"]]
  :main usbtesting.core
  :jvm-opts ["-Djava.library.path=target/native/mac"])
