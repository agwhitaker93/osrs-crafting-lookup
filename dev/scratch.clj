(ns scratch
  (:require [clojure.java.jdbc :as jdbc]
            [osrs-crafting-lookup.components.items :as items]
            [clojure.walk :refer [keywordize-keys]]))
