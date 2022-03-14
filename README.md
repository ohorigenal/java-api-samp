![push-test](https://github.com/ohorigenal/java-api-samp/workflows/push-test/badge.svg?branch=master)

# Weather API
天気情報の登録、取得のAPI実装のサンプル。

フォーマットはJSON

言語：Java

FW：Spring Boot(JPA)

他: Docker,Kubernetes

```
# 実行(簡略化でインメモリDB使用のため実行コマンドのみで動きます)
./gradlew bootRun
```

docker、minikube(local kubernetes)を利用しての実行は下記に記載してます


## IF仕様
#### 共通ヘッダ

**input**

|header|required|概要|
|---|---|---|
|Auth-Token|○|認証用(内部ネットワーク想定)、CASE_INSENSITIVE固定値("auth-token")|
|X-Request-ID| |ない場合作成 |

**output**

|header|type|概要|
|---|---|---|
|X-Request-ID|string| |

### /register (POST)

* 天気情報の登録

**input**

|body key|type|required|概要|
|---|---|---|---|
|location_id|int|○|"1"|
|date|string|○|"20200101" 八桁|
|weather|int|○|"1=Sunny 2=Cloudy 3=Rainy 4=Snowy"|
|comment|string| |一言コメント|

**output**

|key|type|概要|
|---|---|---|
|message|string|"weather registered"|



### /get/{location_id}/{date} (GET)

* 特定の日の天気情報の取得

※サンプルのためlocation_idは1,2のみ(data.sql)

**input**

|path param|type|required|概要|
|---|---|---|---|
|location_id|int|○|1|
|date|string|○|"20200101" 八桁|

**output**

|key|type|概要|
|---|---|---|
|location|string|"新宿"|
|date|string|"20200101" 八桁|
|weather|string|"Sunny Cloudy Rainy Snowy"|
|comment|string|一言コメント|

### /get/apidata (GET)

* 外部APIの東京の5日間の天候情報を整形したものを取得

https://www.metaweather.com/api/location/1118370/

**output**

|key| |type|概要|
|---|---|---|---|
|consolidated_weather| | |5日間の天気情報|
| |weather_state_name|string|天候名|
| |applicable_date|string|日|
| |wind_speed|float|風速|
| |air_pressure|float|大気圧|
| |humidity|int| 湿度 |
|title| |string| 都市 |
|timezone| |string| タイムゾーン |


### エラー共通レスポンス

|key|type|概要|
|---|---|---|
|message|string|"error message"|

## 実行(skaffold)
```
# 事前準備
# dockerのinstall
https://docs.docker.com/get-docker/

# 設定 -> Enable Kubernetes
```

```
# https://skaffold.dev/docs/
# 実行
$ skaffold run --port-forward
```



<details><summary>docker,minikube実行</summary>
## 実行(docker)

```
# 事前準備
# dockerのinstall
https://docs.docker.com/get-docker/
```

リポジトリのルートディレクトリで実行

```
# buildと実行
$ docker build --no-cache -t java-api-samp/api:v1.0 .
$ docker run -p 8080:8080 java-api-samp/api:v1.0

※不要なイメージは削除も必要になると思います
$ docker rmi <image ID> (<image ID> ...) 
```

## 実行(minikube)

```
# 事前準備 
# dockerのinstall
https://docs.docker.com/get-docker/

# minikubeのinstall
https://kubernetes.io/ja/docs/tasks/tools/install-minikube/

# kubectlのinstall
https://kubernetes.io/ja/docs/tasks/tools/install-kubectl/
```

```
# minikube起動
$ minikube start

# namespaceの作成
$ kubectl create namespace minikube

# contextの設定
$ kubectl config set-context $(kubectl config current-context) --namespace=minikube

# contextの確認
$ kubectl config get-contexts

# addonの追加(数分) https://kubernetes.io/ja/docs/tasks/access-application-cluster/ingress-minikube/
$ minikube addons enable ingress

# addonの追加を確認(ingress-controller)
$ kubectl get pods -n kube-system 

# minikubeのdockerを利用(minikube内にdocker imageを保存するため)
$ eval $(minikube docker-env)

# minikube環境を利用しているか確認(minikubeとなっていること)
$ docker info --format '{{json .Name}}'

# タグをつけてビルド
$ docker build --no-cache -t java-api-samp/java:v1.0 . 
$ docker images java-api-samp/java:v1.0

# minikubeへのデプロイ
$ kubectl apply -k ./.k8s/overlay/minikube/

# ingressのADDRESS,HOSTを確認(host設定のため)
$ kubectl get ingress java-api-ingress

# ホストファイル/etc/hostsを編集 ※windowsではC:¥Windows¥System32¥drivers¥etc¥hosts
# macなどでvimで編集するなら
$ sudo vim /etc/hosts
例) 172.168.1.10 compass-j.com
確認したADDRESS HOSTを上記の例の形式で最終行に追加

--完了--
```
</details>

**アクセス例**
```
# URL
# skaffold, docker: http://localhost:8080
# minikube: https://compass-j.com

# /register
$ curl -D - --insecure -X POST -H 'Content-Type: application/json' -H 'Auth-Token: auth-token' -d '{"location_id":1, "date":"20200101", "weather":1, "comment":"good day"}' http://localhost:8080/register

# /get/:location_id/:date
$ curl -D - --insecure -X GET -H 'Auth-Token: auth-token' http://localhost:8080/get/1/20200101

# /get/apidata
$ curl -D - --insecure -X GET -H 'Auth-Token: auth-token' http://localhost:8080/get/apidata
```

**備考** 
```
# 実行jar作成
$ ./gradlew bootJar

# テスト実行
$ ./gradlew test
```
