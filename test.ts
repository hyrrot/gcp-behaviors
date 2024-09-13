import {afterAll, beforeAll, describe, expect, test} from '@jest/globals';
import * as automation from "./automation";


describe("Test infrastructure deployment", () => {

    beforeAll(async () => {
        await automation.deploy();
    }, 60000000);

    afterAll(async () => {
        await automation.destroy();
    },60000000);

    test("should return correct html", async () => {
        const outputs = await automation.getOutputs();
        console.log(outputs);
        expect(outputs.projectId.value).toBe(process.env.PROJECT_ID);
    }, 60000000);



});


// import { expect } from "chai";
// import * as automation from "./automation";

// before(async () => {
//   await automation.deploy();
// });

// after(async () => {
//   await automation.destroy();
// });

// describe("Test infrastructure deployment", () => {
//   it("should return correct html", async () => {
//     await automation
//       .getOutputs()
//       .then((result) => result.url.value)
//       .then((url) => {
//         expect(url).to.be.a("string");
//       });
//   });

// //   it("should not return a 404", async () => {
// //     await automation
// //       .getOutputs()
// //       .then((result) => result.url.value)
// //       .then((url) => {
// //         expect(url).to.be.a("string");
// //         return superagent.get(url);
// //       })
// //       .then((response) => response.statusCode)
// //       .then((statusCode) => {
// //         expect(statusCode).to.equal(200);
// //       });
// //   })
// });