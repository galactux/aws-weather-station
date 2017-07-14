rm -f backend/target/*.war
rm -rf backend/src/main/resources/static
pushd frontend/
rm -rf build/
npm run build
popd
mvn --file backend/pom.xml clean
mkdir -p backend/src/main/resources/
ln -s "$(readlink -f frontend/build/)" backend/src/main/resources/static
mvn --file backend/pom.xml package
echo "========================================================================"
echo "Output WAR file:"
echo "  $(readlink -f backend/target/*.war)"
echo "========================================================================"
