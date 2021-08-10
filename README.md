
# React Native Cybersource Device Fingerprint

## Getting started

`$ yarn add https://github.com/90lucasgabriel/react-native-cybersource-device-fingerprint-webjump`

### Manual installation

#### iOS

1. Add pod 'RNCybersourceDeviceFingerprint', :path => '../node_modules/react-native-cybersource-device-fingerprint/ios' to your Podfile
2. Run pod install from ios folder
3. Run your project (`Cmd+R`)<

#### Android

NOTE: if you use Reactive Native 0.60+ you can autolinking, ignore this step 1.

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.mauriciomartinscruz.CybersourceDeviceFingerprint.RNCybersourceDeviceFingerprintPackage;` to the imports at the top of the file
  - Add `new RNCybersourceDeviceFingerprintPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-cybersource-device-fingerprint'
  	project(':react-native-cybersource-device-fingerprint').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-cybersource-device-fingerprint/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
	dependencies {
		...
		// Cybersource FingerPrint by mauriciomartinscruz
		implementation project(':react-native-cybersource-device-fingerprint')
	}
  	```


## Usage
```ts
import RNCybersourceDeviceFingerprint from 'react-native-cybersource-device-fingerprint';

const getFingerprint = async (): Promise<string> => {
  const orgId = 123456; // org_id do projeto
  const serverURL = 'h.online-metrix.net'; // endpoint que recebe as informações do projeto
  const merchantId = 'braspag_projeto'; // identificação única do projeto com a Braspag

  RNCybersourceDeviceFingerprint?.configure(orgId, serverURL); // envio do org_id e do endpoint para validação
  const { sessionId } = await RNCybersourceDeviceFingerprint?.getSessionID(merchantId); // envio do merchant_id que será concatenado e retorno do session_id

  return sessionId;
};

export default getFingerprint;
```
  
