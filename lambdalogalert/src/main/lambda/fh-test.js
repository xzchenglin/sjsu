'use strict';
console.log('Loading function');

exports.handler = (event, context, callback) => {
    /* Process the list of records and transform them */

console.log(event.records[0]);
console.log(event.records[0].data);

const output = event.records.map((record) => {
    var milliseconds = new Date().getTime();
    const entry = JSON.parse((new Buffer(record.data, 'base64')).toString('utf8'));

            const result = `${milliseconds},${entry.uid},${entry.food},${entry.count}`+"\n";
            const payload = (new Buffer(result, 'utf8')).toString('base64');

            console.log(result);
            console.log(payload);


                return {
                    recordId: record.recordId,
                    result: 'Ok',
                    data: payload,
                };
    });
    console.log(`Processing completed.  Successful records ${output.length}.`);
    callback(null, { records: output });
};
