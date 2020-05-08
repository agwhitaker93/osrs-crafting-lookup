(ns osrs-crafting-lookup.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [osrs-crafting-lookup.core-test]
   [osrs-crafting-lookup.common-test]))

(enable-console-print!)

(doo-tests 'osrs-crafting-lookup.core-test
           'osrs-crafting-lookup.common-test)
