(ns osrs-crafting-lookup.database
  (:import (java.util Calendar)
           (java.sql Timestamp)))

(defn timestamp-now []
  (-> (Calendar/getInstance)
      (.getTimeInMillis)
      (Timestamp.)))
