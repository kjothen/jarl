(ns build
  (:require [clojure.tools.build.api :as b]))

(defn compile-java
  [_]
  (let [opts {:basis (b/create-basis {}),
              :javac-options ["-target" "11" "-source" "11"],
              :class-dir "target/classes"}]
    (b/javac (assoc opts :src-dirs ["core/src/main/java"]))
    (b/compile-clj (assoc opts
                     :src-dirs ["core/src/main/clj" "core/src/main/cljc"
                                "core/src/main/resources"]))
    (b/javac (assoc opts :src-dirs ["api/src/main/java"]))))
