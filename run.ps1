# Le a versao do pom.xml
[xml]$POM = Get-Content "pom.xml"
$VERSION = $POM.project.version

$IMAGE_NAME = "outsera-api:$VERSION"

Write-Host "Versao encontrada no pom: $VERSION"

Write-Host "Gerando JAR..."
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erro no Maven build."
    exit 1
}

Write-Host "Gerando imagem Docker: $IMAGE_NAME"
docker build -t $IMAGE_NAME .
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erro no Docker build."
    exit 1
}

Write-Host "Aplicando deployment..."
kubectl apply -f deployment.yaml
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erro ao aplicar deployment."
    exit 1
}

Write-Host "Aplicando service..."
kubectl apply -f service.yaml
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erro ao aplicar service."
    exit 1
}

Write-Host "Atualizando imagem do deployment..."
kubectl set image deployment/outsera-api outsera-api=$IMAGE_NAME
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erro ao atualizar imagem."
    exit 1
}

Write-Host "Atualizando APP_VERSION..."
kubectl set env deployment/outsera-api APP_VERSION=$VERSION
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erro ao atualizar APP_VERSION."
    exit 1
}

Write-Host "Aguardando rollout..."
kubectl rollout status deployment/outsera-api
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erro no rollout."
    exit 1
}

Write-Host "Publicacao concluida com sucesso. Versao: $VERSION"