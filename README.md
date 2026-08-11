# simulated-user

`simulated-user` is a bounded, deterministic library for evaluating a digital
product with cohorts of **fictional categorical users**. It takes the useful
shape from MatrAIx—correlated persona dimensions and Survey / Chatbot / Web /
App evaluation—without materializing a global population or claiming that an
LLM is a real human.

The formal result is a cohort aggregate. Individual trial data is an internal
execution value, not a prediction about a real person.

## R0 contract

1. A schema is an ordered categorical dependency DAG.
2. `sample-cohort` lazily samples a bounded cohort from conditional weights.
3. The host injects `interact`; this library owns no model, network, browser, or
   app authority.
4. A task is limited to `:product-evaluation` or `:research`, with
   `:decision-impact :none`.
5. Aggregates require at least five trials. PII dimensions, human-grounded
   records, persuasion, targeting, credit, employment, insurance, healthcare,
   and eligibility decisions are structurally rejected.

```clojure
(require '[simulated-user.model :as model]
         '[simulated-user.eval :as eval])

(def schema
  {:source :synthetic
   :order [:age-band :device]
   :dimensions
   {:age-band {:values [:young :older]}
    :device {:values [:mobile :desktop]
             :parents [:age-band]
             :conditional
             {{:age-band :young} [[:mobile 9] [:desktop 1]]
              {:age-band :older} [[:mobile 2] [:desktop 8]]}}}})

(def cohort (model/sample-cohort schema {:seed 42 :n 20}))

(eval/evaluate
  (fn [{:keys [persona task context]}]
    ;; Host-owned model, browser, or deterministic evaluator.
    {:score 0.75 :choice :continue})
  {:task/id :checkout-copy
   :environment :web
   :purpose :product-evaluation
   :decision-impact :none
   :prompt "Would this fictional user continue?"}
  cohort
  {:variant :b})
```

## Repository boundary

- [`cloud-itonami/hakoniwa`](https://github.com/cloud-itonami/hakoniwa) owns
  fictional-world forward simulation and preparedness distributions.
- [`kotoba-lang/persona`](https://github.com/kotoba-lang/persona) owns public
  identity aliases and mail relay decisions.
- [`kotoba-lang/com-surveymonkey`](https://github.com/kotoba-lang/com-surveymonkey)
  owns SurveyMonkey-compatible API behavior.
- [`kotoba-lang/shinka`](https://github.com/kotoba-lang/shinka) owns population
  optimization. It does not model users.

This repository owns only fictional-cohort product evaluation.

## Verification

```bash
clojure -M:test
nbb -cp src:test run-tests.cljs
```

## Sources

- MatrAIx paper: <https://arxiv.org/abs/2608.04205>
- 36Kr overview supplied as the implementation prompt:
  <https://eu.36kr.com/en/p/3932853833759876>

The implementation is original `.cljc`; it does not copy the MatrAIx dataset,
prompts, task corpus, or source code.
